import { Component, Input } from "@angular/core";

declare var $: any;

@Component({
    selector: 'modal',
    templateUrl: './modal.component.html',
    styleUrls: ['./modal.component.css']
})
export class ModalComponent {
    @Input() id: string = "modal";
    @Input() title: string = "";
    @Input() size: string = "mini"; // mini tiny small large
    @Input() type: string = "alert"; // alert confirm custom

    constructor() { } // on create component
    
}