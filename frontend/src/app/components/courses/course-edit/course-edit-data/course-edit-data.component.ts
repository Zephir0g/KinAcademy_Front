import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
// import Editor from "ckeditor5-custom-build";
import Editor from 'ckeditor5-custom-build';
import {faPlus, faArrowRight} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../../axios.service";
import {DataService} from "../../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MatDialog} from "@angular/material/dialog";
import {NgxFileDropEntry} from "ngx-file-drop";
import {environment} from "../../../../../../environments/environment";



export interface DialogData {
  sectionName: string;
  name: string;
}

@Component({
  selector: 'app-course-edit-data',
  templateUrl: './course-edit-data.component.html',
  styleUrls: ['./course-edit-data.component.css']
})
export class CourseEditDataComponent implements OnInit, OnChanges {
  Editor = Editor;
  faPlus = faPlus;
  user = JSON.parse(localStorage.getItem('user') || '{}');
  internalization = JSON.parse(localStorage.getItem('internalization') || '{}');
  faArrowRight = faArrowRight;
  course: any = {};
  languages: any;
  isLoading: boolean = true;
  @Input() isEdit: boolean | undefined;
  @Input() courseUrl !: string;

  imageNotFound: string = environment.imageNotFound;
  isImageDropped = false;

  categories: any = {};

  courseImage: string = '';
  courseDescription: String = '';
  courseShortDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  sections: any;


  sectionInputName: string = '';
  videoInputName: string = '';
  selectedFileUrl: string = '';
  selectedFile: any;


  constructor(private router: Router,
              private titleService: Title,
              private axiosService: AxiosService,
              private data: DataService,
              private spinner: NgxSpinnerService,
              public dialog: MatDialog) {

  }

  ngOnInit(): void {
    this.showSpinner();
    this.loadCourseData();
    this.categories = this.data.getCategories();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isEdit']) {
      const currentValue = changes['isEdit'].currentValue;
      const previousValue = changes['isEdit'].previousValue;

      if (previousValue === true && currentValue === false) {
        //TODO Save course data and send to server
        console.log("Save course data and send to server");
        this.saveCourse();
      }
    }
  }

  loadCourseData() {
    this.fetchCourseDataFromServer();
  }

  fetchCourseDataFromServer() {
    this.getCourseData(this.courseUrl)
      .then((response) => {
        if (response) {
          //TODO Fix title
          this.titleService.setTitle(response.data.name + " | Course");
          this.course = response.data;
          this.hideSpinner();
          this.loadCourseDetails();
        }
      })
      .catch((error) => {
        console.log(error.response.data.message);
        this.hideSpinner();
      });
  }

  loadCourseDetails() {
    if (!this.userIsAuthor()) {
      this.router.navigate(['/course/' + this.courseUrl]);
    }
    this.hideSpinner();
    this.languages = this.data.getLanguages();
    this.courseImage = this.course.imageUrl || this.imageNotFound;
    this.courseDescription = this.course.description;
    this.courseShortDescription = this.course.shortDescription;
    this.courseName = this.course.name;
    this.courseLanguage = this.course.language;
    this.courseCategory = this.course.category;
    this.sections = this.course.sections;
  }

  userIsAuthor(): boolean {
    return this.user.username === this.course.authorUsername;
  }

  getCourseData(url: string): Promise<any> {
    return this.axiosService.requestWithHeaderAuth(
      "GET",
      "/course/" + url + "?username=" + this.user.username,
      null,
      this.user.secure_TOKEN
    );
  }

  addSection() {
    this.sectionInputName = this.sectionInputName.trim();
    if (this.sectionInputName !== undefined && this.sectionInputName !== "" && this.sectionInputName.replace(/\s/g, '') !== "") {
      //check if section with this name already exists
      if (this.sections.some((section: any) => section.name === this.sectionInputName)) {
        alert("Section with this name already exists.");
      } else {
        this.sections.push({name: this.sectionInputName, videos: []});
      }
    }
    this.sectionInputName = '';
  }

  showSpinner() {
    this.spinner.show();
    this.isLoading = true;
  }

  hideSpinner() {
    this.spinner.hide();
    this.isLoading = false;
  }

  onVideoUploaded(event: any) {
    this.selectedFile = event.target.files[0];
    this.selectedFileUrl = '';
    //if selected file is not video mp4,avi,webm then print error
    if (this.selectedFile) {
      const allowedTypes = ['video/mp4', 'video/webm', 'video/avi'];
      if (!allowedTypes.includes(this.selectedFile.type)) {
        alert('Please, choose file .avi, .mp4 or .webm.');
        return;
      } else {
        this.selectedFileUrl = URL.createObjectURL(this.selectedFile);
      }
    }
  }

  async onSubmitVideo(sectionName: string) {
    //TODO create loading spinner for video upload
    const videoName = this.videoInputName;
    this.addVideoToSection(sectionName, videoName);
    this.data.getVideoUrlFromServer(this.courseUrl, this.selectedFile)
      .then((response) => {
        if (response) {
          //TODO Fix this
          this.updateVideoUrl(sectionName, videoName, response.data.urlToVideo);
        }
      }).catch((error) => {
      console.log(error.response.data.message);
      console.log(error.response.status);
      this.data.errorHandler(error);
    });

    this.videoInputName = '';
    this.selectedFileUrl = '';
    this.selectedFile = null;
  }

  async addVideoToSection(sectionName: string, videoName: string) {
    const section = this.sections.find((section: any) => section.name === sectionName);

    if (section) {
      section.videos.push({name: videoName, urlToVideo: ''});
    }
  }

  async updateVideoUrl(sectionName: string, videoName: string, url: string) {
    const section = this.sections.find((section: any) => section.name === sectionName);

    const videoIndex = section.videos.findIndex((video: any) => video.name === videoName);
    if (videoIndex !== -1) {
      section.videos[videoIndex].urlToVideo = url;
    }
  }

  saveCourse() {
    this.course.category = this.courseCategory;
    this.course.language = this.courseLanguage;
    //if this.courseImage equal to imageNotFound then set empty string
    this.course.imageUrl = this.courseImage === this.imageNotFound ? '' : this.courseImage;
    this.course.description = this.courseDescription;
    this.course.shortDescription = this.courseShortDescription;
    this.course.sections = this.sections;
    this.data.updateCourse(this.course);
  }

  onFileDropped(files: NgxFileDropEntry[]) {
    console.log("File dropped");
    //TODO Realize this
    this.isImageDropped = false;

  }

  onDragOver(event: any) {
    if (!this.isEdit) return;
    event.preventDefault();
    this.isImageDropped = event.dataTransfer && event.dataTransfer.types.includes('Files');
    console.log("Drag over");
  }

  protected readonly JSON = JSON;
}
