import { Component, Input, AfterViewInit, Output, EventEmitter, ChangeDetectorRef, OnInit, SimpleChanges } from "@angular/core";
import { Calendar } from "models/";
import { dateLocale, digit } from "app/baiwa/common/helpers";
import { NgControl, FormGroup, FormControl } from "@angular/forms";

declare var $: any;

@Component({
    selector: 'ui-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements AfterViewInit, OnInit {

    @Input() calendar: Calendar;
    @Input() disableCalendar: NgControl;
    @Input() call: boolean;

    @Output() calendarValue: EventEmitter<string> = new EventEmitter<string>();

    constructor(private cdRef: ChangeDetectorRef) {
    }

    ngOnInit() {
        // console.log(this.calendar);
    }

    ngAfterViewInit() {
        const { type, formatter, local, initial, minDate, maxDate, startId, endId } = this.calendar;
        $(`#${this.calendar.calendarId}`).calendar({
            type: type || 'date',
            text: DateConstant.text,
            formatter: DateConstant.formatter(formatter || '', local || 'en'),
            onChange: this.onChange,
            touchReadonly: true,
            initialDate: initial || null,
            minDate: minDate || null,
            maxDate: maxDate || null,
            startCalendar: $(`#${startId}`) || null,
            endCalendar: $(`#${endId}`) || null,
        })
        if (initial) {
            const init = new Date(Date.UTC(initial.getFullYear(), initial.getMonth(), initial.getDate()));
            $(`#${this.calendar.calendarId}`).calendar('set date', init);
        }
        this.cdRef.detectChanges();
    }

    ngOnChanges(changes: SimpleChanges) {
        const { type, formatter, local, initial, minDate, maxDate, startId, endId } = this.calendar;
        $(`#${this.calendar.calendarId}`).calendar({
            type: type || 'date',
            text: DateConstant.text,
            formatter: DateConstant.formatter(formatter || '', local || 'en'),
            onChange: this.onChange,
            touchReadonly: true,
            initialDate: initial || null,
            minDate: minDate || null,
            maxDate: maxDate || null,
            startCalendar: $(`#${startId}`) || null,
            endCalendar: $(`#${endId}`) || null,
        })
        if (initial) {
            const init = new Date(Date.UTC(initial.getFullYear(), initial.getMonth(), initial.getDate()));
            $(`#${this.calendar.calendarId}`).calendar('set date', init);
        }
    }

    onChange = (date, text, mode) => {
        this.calendarValue.emit(text);
    }
}

class DateConstant {
    public static text: any = {
        days: ["อา", "จ", "อ", "พ", "พฤ", "ศ", "ส"],
        months: [
            "มกราคม",
            "กุมภาพันธ์",
            "มีนาคม",
            "เมษายน",
            "พฤษภาคม",
            "มิถุนายน",
            "กรกฎาคม",
            "สิงหาคม",
            "กันยายน",
            "ตุลาคม",
            "พฤศจิกายน",
            "ธันวาคม"
        ],
        monthsShort: [
            "ม.ค.",
            "ก.พ.",
            "มี.ค.",
            "เม.ย.",
            "พ.ค.",
            "มิ.ย.",
            "ก.ค.",
            "ส.ค.",
            "ก.ย.",
            "ต.ค.",
            "พ.ย.",
            "ธ.ค."
        ],
        today: "วันนี้",
        now: "เดี๋ยวนี้",
        am: "ก่อนบ่าย",
        pm: "หลังบ่าย"
    };

    public static formatter = (txt: string = '', local: string = 'en') => {
        switch (txt) {
            case 'mmmm yyyy':
                return {
                    header: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[2];
                    },
                    date: (date, mode, settings) => {
                        let month = date.getMonth();
                        let _year = DateConstant[`${local}Date`](date).split("/")[2];
                        return DateConstant.text.months[month] + " " + _year;
                    }
                };
            case 'MM/yyyy':
                return {
                    header: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[2];
                    },
                    date: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[1] + "/" + DateConstant[`${local}Date`](date).split("/")[2];
                    }
                };
            case 'dd/mm/yyyy':
                return {
                    header: (date, mode, settings) => {
                        let month = date.getMonth();
                        let _year = DateConstant[`${local}Date`](date).split("/")[2];
                        return DateConstant.text.months[month] + " " + _year;
                    },
                    date: (date, settings) => {
                        return DateConstant[`${local}Date`](date);
                    }
                };
            case 'yyyy':
                return {
                    header: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[2];
                    },
                    date: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[2];
                    }
                };
            default:
                return {
                    header: (date, mode, settings) => {
                        let month = date.getMonth();
                        let _year = DateConstant[`${local}Date`](date).split("/")[2];
                        return DateConstant.text.months[month] + " " + _year;
                    },
                    date: (date, settings) => {
                        return DateConstant[`${local}Date`](date);
                    }
                };
        }
    }

    public static thDate = (date: Date) => {
        if (date.getFullYear() > (new Date().getFullYear() + 500)) {
            return `${digit(date.getDate())}/${digit(date.getMonth() + 1)}/${date.getFullYear()}`;
        } else {
            const _date = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())); // Date.UTC()
            const __dat = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ");
            const ___da = __dat[0].split("/");
            return `${digit(___da[0])}/${digit(___da[1])}/${___da[2]}`;
        }
    }

    public static enDate = (date: Date) => {
        return `${digit(date.getDate())}/${digit(date.getMonth() + 1)}/${date.getFullYear()}`;
    }
}