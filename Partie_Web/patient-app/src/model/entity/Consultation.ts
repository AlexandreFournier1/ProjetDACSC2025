import type { Entity } from '@/model/entity/Entity'

export interface Consultation extends Entity {
    date: string
    hour: string
    doctor_name: string
    specialty: string
    reason: string | null
}
