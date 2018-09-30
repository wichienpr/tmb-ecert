import { FormGroup } from "@angular/forms";

export interface Calendar {
    [X: string]: any;
    type?: string;
    formatter?: string;
    calendarId: string;
    calendarName: string;
    formGroup: FormGroup;
    formControlName: string;
    placeholder?: string;
    icon?: string;
}