import {Product} from "./model";
import {request} from "./utils/http";

const baseUrl = "http://localhost:9000"

export function findProduct(barcode: string): Promise<Product> {
  return request<Product>({
    method: "GET",
    url: `${baseUrl}/products/${barcode}`
  })
}