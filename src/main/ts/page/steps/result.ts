import {h} from "kaiju";
import {State} from "../store";

export default function (state: State) {
  console.log(state)
  return h("div", [
    renderMagasins(state)
  ])
}

function renderMagasins(state: State) {
  return h("ul", state.magasinsWithPrice.map(magasinWithPrice =>
    h("li", `${magasinWithPrice.magasin.name} (${magasinWithPrice.price})`)
  ));
}