import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Certificate } from 'models/';
declare var $: any;
@Component({
  selector: 'app-rep03000',
  templateUrl: './rep03000.component.html',
  styleUrls: ['./rep03000.component.css']
})
export class Rep03000Component implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {}
  ngAfterViewInit() {
    $("#table").DataTable();
  }
  

  searchData(): void {
    this.showData = true;
  }
  
  
  clearData(): void {
    this.showData = false;
  }

}
