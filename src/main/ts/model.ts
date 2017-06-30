export interface Product {
  barcode: string
  name: string
}

export interface Magasin {
  id: string
  name: string
}

export interface MagasinWithPrice {
  magasin: Magasin
  price: number
}