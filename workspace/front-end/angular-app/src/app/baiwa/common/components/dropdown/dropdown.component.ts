import { Component, AfterViewInit, Input, Output } from "@angular/core";
import { EventEmitter } from '@angular/core';

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
        "[style.width.%]" : "100"
    }
})
export class DropdownComponent implements AfterViewInit {


    @Input() type: string;

    @Input() dropdownId: string;
    @Input() dropdownName: string;
    @Input() placehold: string;

    @Input() values: any[];
    @Input() valueName: string;
    @Input() labelName: string;

    @Output() valueChange: EventEmitter<any> = new EventEmitter<any>();

    constructor() {}

    ngAfterViewInit() {
        $(`#${this.dropdownId}`).dropdown().css('width', '100%');
    }

    onChange(e) {
        this.valueChange.emit(e.target.value);
    }

}