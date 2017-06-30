import Store from "kaiju/store";
import * as action from "./actions";
import {update} from "immupdate";
import {Magasin, MagasinWithPrice, Product} from "../model";

export type Step = "list" | "result"

export interface State {
  step: Step
  articles: Array<Product>
  magasinsWithPrice: Array<MagasinWithPrice>
}

export const initialState: State = {
  step: "list",
  articles: [],
  magasinsWithPrice: []
}

export const store = Store<State>(initialState, on => {
  on(action.addProduct, (state, article) =>
    update(state, {
      articles: [].concat(state.articles).concat([article])
    })
  )

  on(action.showResult, (state, magasinsWithPrice) =>
    update(state, {
      magasinsWithPrice: magasinsWithPrice,
      step: "result"
    })
  )

  on(action.changeStep, (state, step) => update(state, {step}))
})