import {Component, ConnectParams, RenderParams, startApp} from "kaiju";
import classModule from "snabbdom/modules/class";
import propsModule from "snabbdom/modules/props";
import attrsModule from "snabbdom/modules/attributes";
import {update} from "immupdate";
import * as api from "./api";

import {initialState, State, store} from "./page/store";
import * as actions from "./page/actions";
import page from "./page/page";
import scanBarcode from "./sensors/barcode";
import {Observable} from "kaiju/observable";

interface Props {
  key: ""
}

const app = (() => {
  const initState = () => initialState

  const connect = ({on}: ConnectParams<Props, State>) => {
    on(store.state, (state, newState) => update(state, newState))

    on(actions.changeStep, (state, stepName) => {
      store.send(actions.changeStep(stepName))
      return state
    })

    on(actions.scanBarcode, (state) => {
      const observable = Observable.fromPromise(scanBarcode().then(api.findProduct))
      on(observable, (state, result) => {
        result.map(product => store.send(actions.addProduct(product)))
      })
      return state
    })
  }

  const render = ({state}: RenderParams<Props, State>) => page(state)

  return Component<Props, State>({name: 'app', initState, connect, render})
})()

startApp({
  app: app,
  snabbdomModules: [
    classModule,
    propsModule,
    attrsModule
  ],
  elm: document.querySelector("#app")
})