import { Component, Input, AfterViewInit, Output, EventEmitter } from "@angular/core";

declare var $: any;

@Component({
    selector: 'ui-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements AfterViewInit {

    @Input() calendarId: string;

    @Output() calendarValue: EventEmitter<string> = new EventEmitter<string>();

    constructor() { }

    ngAfterViewInit() {
        $(`#${this.calendarId}`).calendar({
            type: "date",
            onChange:  this.onChange
        });
    }

    onChange(date, text, mode) {
        console.log(text);
        //this.calendarValue.emit(text);
    }
}