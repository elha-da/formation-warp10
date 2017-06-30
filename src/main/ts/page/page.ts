import {State} from "./store";
import list from "./steps/list";
import result from "./steps/result";

export default function (state: State) {
  return [
    renderContent(state)
  ]
}

function renderContent(state: State) {
  switch (state.step) {
    case "list":
      return list(state)
    case "result":
      return result(state)
  }
}