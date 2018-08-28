import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-output-vat',
  templateUrl: './output-vat.component.html',
  styleUrls: ['./output-vat.component.css']
})
export class OutputVATComponent implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {
  }

  searchData(): void {
    this.showData = true;
  }
  
  clearData(): void {
    this.showData = false;
  }

}
