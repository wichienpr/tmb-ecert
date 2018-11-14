import { Component, Input, AfterViewInit, Output, EventEmitter, ChangeDetectorRef, OnInit, AfterContentInit } from "@angular/core";
import { Calendar, CalendarType, CalendarFormatter, CalendarLocal } from "models/";
import { dateLocale, digit } from "app/baiwa/common/helpers";
import { NgControl, FormGroup, FormControl, AbstractControl } from "@angular/forms";
import * as moment from 'moment';

declare var $: any;

@Component({
    selector: 'ng-calendar',
    templateUrl: './ng-calendar.component.html',
    exportAs: "ng-calendar"
})
export class NgCalendarComponent implements AfterViewInit, OnInit {

    @Input("config") config: NgCalendarConfig;

    constructor(private cdRef: ChangeDetectorRef) {
    }

    public configObj: any;

    ngOnInit() {

        this.configObj = Object.assign({
            type: CalendarType.DATE,
            formatter: CalendarFormatter.DEFAULT,
            local: CalendarLocal.TH, // CalendarLocal.EN
            placeholder: "กรุณาเลือก"
        }, this.config);

        // console.log("config", this.configObj);

        if (this.config.formControl == null) {
            throw new Error("ระบุ formcontrol ไม่ถูกต้อง กรุณาตรวจสอบ formcontrol")
        }
    }

    ngAfterViewInit() {
        // console.log("ngAfterViewInit" , new Date().getTime());

        this.refresh();

    }

    refresh() {
        const { id, type, formatter, local, startCalendar, endCalendar, formControl } = this.configObj;

        $(`#${id}`).calendar({
            type: type || 'date',
            text: DateConstant.text,
            formatter: DateConstant.formatter(formatter || '', 'th'), // local || 'en'
            onChange: this.onChange,
            touchReadonly: true,
            initialDate: null,
            minDate: null,
            maxDate: null,
            startCalendar: (startCalendar) ? $(`#${startCalendar}`) : null,
            endCalendar: (endCalendar) ? $(`#${endCalendar}`) : null,
        })
        if (formControl.value) {
            this.setValue(new Date(moment(formControl.value, 'DD/MM/YYYY').format()));
        }
    }

    onChange = (date, text, mode) => {
        const { formControl } = this.configObj;
        formControl.patchValue(text);
    }

    setValue(inputDate: Date) {
        const { id } = this.configObj;
        $(`#${id}`).calendar('set date', inputDate);
    }
}

//-----------------------------------------------------------------------------------


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
        local = 'th';
        switch (txt) {
            case 'mmmm yyyy':
                return {
                    header: (date, mode, settings) => {
                        return DateConstant[`${local}Date`](date).split("/")[2];
                    },
                    cell: (cell, date, cellOptions) => {
                        if (cellOptions.mode == "year") {
                            cell[0].innerText = DateConstant[`${local}Date`](date).split("/")[2];
                        }
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
                    cell: (cell, date, cellOptions) => {
                        if (cellOptions.mode == "year") {
                            cell[0].innerText = DateConstant[`${local}Date`](date).split("/")[2];
                        }
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
                    cell: (cell, date, cellOptions) => {
                        if (cellOptions.mode == "year") {
                            cell[0].innerText = DateConstant[`${local}Date`](date).split("/")[2];
                        }
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
                    cell: function (cell, date, cellOptions) {
                        cell[0].innerText = DateConstant[`${local}Date`](date).split("/")[2];
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
                    cell: (cell, date, cellOptions) => {
                        if (cellOptions.mode == "year") {
                            cell[0].innerText = DateConstant[`${local}Date`](date).split("/")[2];
                        }
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

export interface NgCalendarConfig {
    id: string
    formControl: AbstractControl
    startCalendar?: string
    endCalendar?: string,
    placeholder?: string,
    formatter?: string,
    type?: string
}