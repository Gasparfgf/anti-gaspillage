export interface Offer {
  id: number;
  title: string;
  description: string;
  price: number;
  merchantId: number;
  merchantName: string;
}

export interface OfferRequest {
  title: string;
  description: string;
  price: number;
}