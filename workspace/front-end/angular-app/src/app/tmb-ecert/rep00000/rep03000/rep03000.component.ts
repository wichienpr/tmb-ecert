import { Component, OnInit } from '@angular/core';
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
