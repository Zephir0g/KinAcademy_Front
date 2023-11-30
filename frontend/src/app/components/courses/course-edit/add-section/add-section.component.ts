import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef,} from '@angular/material/dialog';
import {CourseEditDataComponent, DialogData} from "../course-edit-data/course-edit-data.component";

@Component({
  selector: 'app-add-section',
  templateUrl: './add-section.component.html',
  styleUrls: ['./add-section.component.css']
})
export class AddSectionComponent {
  constructor(
    public dialogRef: MatDialogRef<CourseEditDataComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {
  }

  onNoClick() {
    this.dialogRef.close();
  }
}
