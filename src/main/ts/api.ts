import {MagasinWithPrice, Product} from "./model";
import {request} from "./utils/http";

const baseUrl = "http://localhost:9000"

export function findProduct(barcode: string): Promise<Product> {
  return request<Product>({
    method: "GET",
    url: `${baseUrl}/products/${barcode}`
  })
}

export function lowerPriceNear(barcode: string, lat: number, lon: number): Promise<Array<MagasinWithPrice>> {
  return request<Array<MagasinWithPrice>>({
    method: "GET",
    url: `${baseUrl}/products/${barcode}/searchMagasin?lat=${lat}&lon=${lon}`
  })
}