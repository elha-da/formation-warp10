import Store from "kaiju/store";
import * as action from "./actions";
import {update} from "immupdate";
import {Product} from "../model";

export type Step = "list" | "result"

export interface State {
  step: Step
  articles: Array<Product>
}

export const initialState: State = {
  step: "list",
  articles: []
}

export const store = Store<State>(initialState, on => {
  on(action.addProduct, (state, article) => {
    console.log(state, article, [].concat(state.articles).concat([article]))
      return update(state, {
        articles: [].concat(state.articles).concat([article])
      })
    }
  )

  on(action.changeStep, (state, step) => update(state, {step}))
})