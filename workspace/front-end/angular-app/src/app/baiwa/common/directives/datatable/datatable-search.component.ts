import { AfterViewInit, OnInit, Component, Input } from "@angular/core";
import { DatatableDirective } from "./datatable.directive";

@Component({
    selector: 'ng-datatable-search',
    template : `
                <div class="ui grid">
                  <div class="six wide column">
                    Show
                    <select class="ui search dropdown" (change)="searchTable.chagePage($event.target.value)">
                      <option [value]="item" *ngFor="let item of searchTable?.pageLength"> {{item}}</option>
                    </select>
                    entries
  
                  </div>
                  <div class="six wide column"></div>
                </div>
    `
    // templateUrl: './calendar.component.html',
    // styleUrls: ['./calendar.component.css']
})
export class DatatableSearchComponent implements AfterViewInit, OnInit {


    @Input("ng-datatable")
    searchTable: DatatableDirective;

    ngOnInit(){

    }

    ngAfterViewInit(){
    //    console.log("ngAfterViewInit", this.searchTable);
    }

}