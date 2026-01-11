import type { Entity } from "./Entity";

export interface Doctor extends Entity {
    specialty_id: string,
    lastName: string,
    firstName: string,
}