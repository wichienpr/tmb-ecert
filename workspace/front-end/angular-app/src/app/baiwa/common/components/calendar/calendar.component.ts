import { Component, Input, AfterViewInit, Output, EventEmitter } from "@angular/core";
import { Calendar } from "models/";

declare var $: any;

@Component({
    selector: 'ui-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements AfterViewInit {

    @Input() calendar: Calendar;

    @Output() calendarValue: EventEmitter<string> = new EventEmitter<string>();

    constructor() { }

    ngAfterViewInit() {
        $(`#${this.calendar.calendarId}`).calendar({
            type: this.calendar.type || 'date',
            onChange:  this.onChange
        });
    }

    onChange = (date, text, mode) => {
        this.calendarValue.emit(text);
    }
}