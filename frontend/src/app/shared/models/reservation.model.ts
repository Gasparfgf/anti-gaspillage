export type ReservationStatus = 'RESERVED' | 'PICKED_UP';

export interface Reservation {
  id: number;
  offerId: number;
  offerTitle: string;
  userId: number;
  userName: string;
  status: ReservationStatus;
}

export interface ReservationRequest {
  offerId: number;
}