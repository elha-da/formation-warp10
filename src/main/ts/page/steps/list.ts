import {h} from "kaiju";
import {State} from "../store";
import {getResult, scanBarcode} from "../actions";

export default function (state: State) {
  return h("div", [
    renderArticles(state),
    renderButtons()
  ])
}

function renderArticles(state: State) {
  return h("ul", state.articles.map(article =>
    h("li", article.name)
  ));
}

function renderButtons() {
  return h("div", [
    h("button", { events: { click: scanBarcode } }, "Scanner un article"),
    h("button", { events: { click: getResult } }, "Terminer")
  ])
}