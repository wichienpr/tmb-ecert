import { AfterViewInit, OnInit, Input, Component } from "@angular/core";
import { DatatableDirective } from "./datatable.directive";

@Component({
    selector: 'ng-datatable-footer',
    template : `
        <div class="ui grid">
            <div class="eight wide column">Showing {{searchTable.intshow}} to {{searchTable.intin}} of
            {{searchTable.intof}} entries</div>
            <div class="eight wide column right aligned">
            
                <div class="ui small basic buttons">
                    <button class="ui labeled icon button" [disabled]="searchTable.page == 1 " (click)="searchTable.back()" >
                        <i class="left chevron icon"></i>
                        Back
                    </button>

                    <div class="ui button" *ngFor="let item of searchTable.buttons" [ngClass]="{'active': (item == searchTable.page)}" (click)="searchTable.nextPage(item)"> {{item}}</div>


                    <button class="ui right labeled icon button"  [disabled]="searchTable.totalpage == searchTable.page" (click)="searchTable.next()" >
                        Next
                        <i class="right chevron icon"></i>
                    </button>
                </div>

            
            </div>
        </div>
    `
    // templateUrl: './calendar.component.html',
    // styleUrls: ['./calendar.component.css']
})
export class DatatableFooterComponent implements AfterViewInit, OnInit {


    @Input("ng-datatable")
    searchTable: DatatableDirective;

    ngOnInit(){

    }

    ngAfterViewInit(){
    //    console.log("ngAfterViewInit", this.searchTable);
    }

}