import {
    Component,
    OnDestroy,
    Input,
    Output,
    EventEmitter,
    ContentChild,
    TemplateRef,
    AfterViewInit
} from "@angular/core";

declare var $: any;

@Component({
    selector: "ui-modal",
    templateUrl: "./modal.component.html",
    styleUrls: ["./modal.component.css"]
})
export class ModalComponent implements AfterViewInit, OnDestroy {
    /**
     * Contents inside selector
     */
    @ContentChild(TemplateRef) templateRef: TemplateRef<any>;
    /**
     * string of modal
     */
    @Input() modalId: string;
    @Input() title: string;

    /**
     * custom class
     */
    @Input() class: string;

    /**
     * @enum alert confirm custom
     */
    @Input() type: string;

    /**
     * works on alert
     * @enum confirm notify 
     */
    @Input() for: string;

    /**
     * @enum mini tiny small large
     */
    @Input() size: string;

    /**
     * works on type = 'confirm'
     */
    @Input() approveMsg: string;
    @Input() rejectMsg: string;

    /**
     * works on type = 'confirm'
     * @return true | false
     */
    @Output() approve: EventEmitter<boolean>;
    @Output() reject: EventEmitter<boolean>;

    constructor() {
        this.approve = new EventEmitter<boolean>();
        this.reject = new EventEmitter<boolean>();
    }

    ngAfterViewInit() {
        // On initial modal
        $(`#${this.modalId}`).modal({ centered: false, autofocus: false });
    }

    ngOnDestroy() {
        // On leave page to remove modal
        $(`#${this.modalId}`).remove();
    }

    onApproved(): void {
        this.approve.emit(true);
    }

    onRejected(): void {
        this.reject.emit(true);
    }

}