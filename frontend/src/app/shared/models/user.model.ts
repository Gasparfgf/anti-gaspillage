export type Role = 'ADMIN' | 'MERCHANT' | 'CLIENT';

export interface User {
    id: number;
    firstname: string;
    surname: string;
    email: string;
    address: string;
    birthDate: string;
    role: Role;
}