import { FormGroup } from "@angular/forms";

export interface Calendar {

    [X: string]: any;
    type?: CalendarType;
    formatter?: CalendarFormatter;
    local?: CalendarLocal;

    calendarId: string;
    calendarName: string;
    formGroup: FormGroup;
    formControlName: string;
    placeholder?: string;
    minDate?: Date;
    maxDate?: Date;
    startId?: String;
    endId?: String;
    initial?: Date;
    icon?: string;
}

export const CD = {
    Formatter: {
        MMMMyyyy: "mmmm yyyy",
        MMyyyy: "MM/yyyy",
        ddMMyyyy: "dd/mm/yyyy",
        yyyy: "yyyy",
        DEFAULT: ""
    },
    Local: {
        TH: "th",
        EN: "en"
    },
    Type: {
        DATE: "date",
        MONTH: "month",
        YEAR: "year"
    }
}

export enum CalendarFormatter{
    MMMMyyyy = "mmmm yyyy",
    MMyyyy = "MM/yyyy",
    ddMMyyyy = "dd/mm/yyyy",
    yyyy = "yyyy",
    DEFAULT = ""
}

export enum CalendarLocal {
    TH = "th",
    EN = "en"
}

export enum CalendarType {
    DATE = "date",
    MONTH = "month",
    YEAR = "year"
}