import {h} from "kaiju"
import {State} from "../store"
import {scanBarcode} from "../actions"

export default function (state: State) {
  return h("div", [
    renderArticles(state),
    renderButtons()
  ])
}

function renderArticles(state: State) {
  console.log(state.articles)
  return h("ul", state.articles.map(article =>
    h("li", article.name)
  ));
}

function renderButtons() {
  return h("div", [
    h("button", { events: { click: scanBarcode } }, "Scanner un article"),
    h("button", "Terminer")
  ])
}