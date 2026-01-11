import type { Specialty } from "./entity/Specialty";
import type { SpecialtyVM } from "./viewmodel/SpecialtyVM";

export interface SpecialtyAccessLayer {
    load(specialtyVM?: SpecialtyVM): Promise<Specialty[]>
}