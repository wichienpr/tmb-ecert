import { Component, OnInit, AfterViewInit } from "@angular/core";

declare var $: any;

@Component({
    selector: "layout",
    templateUrl: "./layout.component.html",
    styleUrls: ["./layout.component.css"]
})
export class LayoutComponent implements OnInit {

    constructor() { }

    /**
     * modal_ids for toggle show/hide
    */
    modal: string[] = ["modal1", "modal2", "modal3"];

    ngOnInit() { }

    openModal(id) {
        $('#' + id).modal('show');
    }

    closeModal(id) {
        $('#' + id).modal('hide');
    }

}