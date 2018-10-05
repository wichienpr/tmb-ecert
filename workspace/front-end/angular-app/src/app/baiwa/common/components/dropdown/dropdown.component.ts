import { Component, AfterViewInit, Input, Output } from "@angular/core";
import { EventEmitter } from '@angular/core';
import { Dropdown } from "models/";

declare var $: any;

@Component({
    selector: "ui-dropdown",
    templateUrl: "./dropdown.component.html",
    styles: [
        `.search {
            min-width: 100% !important;
        }`
    ],
    host: {
        "[style.width.%]": "100"
    }
})
export class DropdownComponent implements AfterViewInit {

    @Input() dropdown: Dropdown;
    @Output() valueChange: EventEmitter<any> = new EventEmitter<any>();

    constructor() { }

    ngAfterViewInit() {
        $(`#${this.dropdown.dropdownId}`).dropdown({ forceSelection: false }).css('width', '100%');
    }

    onChange(e) {
        this.valueChange.emit(e.target.value);
    }

}