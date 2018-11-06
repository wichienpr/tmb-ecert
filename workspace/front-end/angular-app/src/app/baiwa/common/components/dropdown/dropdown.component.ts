import { Component, AfterViewInit, Input, Output, OnChanges, SimpleChanges } from "@angular/core";
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
    },
    exportAs: "ui-dropdown"
})
export class DropdownComponent implements AfterViewInit, OnChanges {

    @Input() dropdown: Dropdown;
    @Input() selected: string;
    @Input() reset: boolean;
    @Output() valueChange: EventEmitter<any> = new EventEmitter<any>();

    constructor() { }

    ngAfterViewInit() {
        $(`#${this.dropdown.dropdownId}`).dropdown({ forceSelection: true, clearable: true }).css('width', '100%');
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.selected) {
            $(`#${this.dropdown.dropdownId}`).dropdown('set selected', changes.selected.currentValue);
        }
        if (changes.reset) {
            if (changes.reset.currentValue == true) {
                $(`#${this.dropdown.dropdownId}`).dropdown('restore defaults');
            }
        }
    }

    onChange(e) {
        this.valueChange.emit(e.target.value);
    }

    clear() {
        let f = this.dropdown.formGroup.get(this.dropdown.formControlName);
        f.patchValue("");
        $(`#${this.dropdown.dropdownId}`).dropdown('clear');
    }
}