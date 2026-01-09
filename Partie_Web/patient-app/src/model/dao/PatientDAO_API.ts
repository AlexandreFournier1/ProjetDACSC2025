import type { PatientVM } from "@/model/viewmodel/PatientVM";
import type { Patient } from "../entity/Patient";
import type { PatientAccessLayer } from "../PatientAccessLayer";

export class PatientNotFoundError extends Error {
    constructor(message: string) {
        super(message);
    }
}

export class PatientDAO_API implements PatientAccessLayer {
    private selectedPatient: Array<Patient>;
    private API_ENDPOINT: string = "http://localhost:8080/api/patients";

    constructor() {
        this.selectedPatient = [];
    }

    public getList(): Array<Patient> {
        return this.selectedPatient;
    }

    public async load(patientVM?: PatientVM): Promise<Array<Patient>> {
        this.selectedPatient = [];
        let url = this.API_ENDPOINT;
        if (patientVM) {
            const params = new URLSearchParams();

            if (patientVM.id) {
                params.append('id', patientVM.id);
            }

            if (patientVM.last_name) {
                params.append('last_name', patientVM.last_name);
            }

            if (patientVM.first_name) {
                params.append('first_name', patientVM.first_name);
            }

            if (patientVM.birth_date) {
                params.append('birth_date', patientVM.birth_date);
            }

            url += `?${params.toString()}`;
        }

        const res = await fetch(url);

        if (res.ok) {
            this.selectedPatient = await res.json();
        } else {
            throw new PatientNotFoundError("Erreur lors du chargement des patients");
        }

        return this.selectedPatient;
    }

    public async save(patient: Patient): Promise<void> {
        if (patient.id == null) {
            const newPatient: Patient = {
                id: patient.id,
                last_name: patient.last_name,
                first_name: patient.first_name,
                birth_date: patient.birth_date
            }

            const res = await fetch(this.API_ENDPOINT, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newPatient)
            })

            if (!res.ok) {
                throw new PatientNotFoundError("Erreur lors de l'ajout du patient")
            }

            const addedPatient = await res.json()
            console.log("Patient ajouté :", addedPatient)
        } 
    }

    public async delete(item: string | Patient): Promise<void> {
        
    }
}