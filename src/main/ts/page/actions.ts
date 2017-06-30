import { Message } from "kaiju"
import {MagasinWithPrice, Product} from "../model";
import {Step} from "./store";

export const addProduct = Message<Product>("addProduct")

export const changeStep = Message<Step>("changeStep")

export const scanBarcode = Message("scanBarcode")

export const getResult = Message("getResult")

export const showResult = Message<Array<MagasinWithPrice>>("setResult")