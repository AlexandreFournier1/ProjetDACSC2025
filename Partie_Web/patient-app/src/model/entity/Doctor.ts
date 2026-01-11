import type { Entity } from "./Entity";

export interface Doctor extends Entity {
    specialty_id: string,
    last_name: string,
    first_name: string,
}