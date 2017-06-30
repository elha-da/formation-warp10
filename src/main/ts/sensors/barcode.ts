import Quagga from "quagga";
import {withIdentity} from "../utils/function";

export default function () {
  return initQuagga()
    .then(withIdentity(showCameraCanvas))
    .then(() => new Promise<string>((resolve) => {
      Quagga.start();
      const reads: Array<string> = [];
      const onDetected = (data: any) => {
        reads.push(data.codeResult.code);
        const normalizedRead = normalize(reads)
        if (normalizedRead) {
          console.log(normalizedRead)
          Quagga.offDetected(onDetected)
          Quagga.stop()
          resolve(normalizedRead)
        }
      }
      Quagga.onDetected(onDetected)
    }))
    .then(withIdentity(hideCameraCanvas))
}

function initQuagga(): Promise<void> {
  return new Promise((resolve, reject) =>
    Quagga.init(
      {
        inputStream: {
          name: "Live",
          type: "LiveStream",
          target: document.querySelector('#barcode') // TODO: how to do it better?
        },
        decoder: {
          readers: ["ean_reader"]
        },
        constraints: {
          width: "100%",
          height: "100%"
        }
      },
      (err: any) => err ? reject(err) : resolve()
    )
  );
}

function showCameraCanvas() {
  barcodeElement().classList.remove("hidden")
}

function hideCameraCanvas() {
  barcodeElement().classList.add("hidden")
}

const barcodeElement = () => document.querySelector("#barcode")

// We need to normalise reads because of some errors that can happen sometimes.
// This normalization will give us a value appearing more than 3 times, so we can considerate it pretty valid.
function normalize(reads: Array<string>): string | null {
  console.log("normalizing", reads)
  const keys: any = {};
  reads.forEach(read => {
    if (keys[read]) keys[read]++
    else keys[read] = 1
  })
  return Object.getOwnPropertyNames(keys).find(_ => keys[_] > 5)
}