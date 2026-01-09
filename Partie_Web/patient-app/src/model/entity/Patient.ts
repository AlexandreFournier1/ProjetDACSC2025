import type { Entity } from "./Entity";

export interface Patient extends Entity {
    last_name: string,
    first_name: string,
    birth_date: string
}