import { Message } from "kaiju"
import {Product} from "../model";
import {Step} from "./store";

export const addProduct = Message<Product>("addProduct")

export const changeStep = Message<Step>("changeStep")

export const scanBarcode = Message("scanBarcode")