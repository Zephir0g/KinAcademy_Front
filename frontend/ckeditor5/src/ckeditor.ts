/**
 * @license Copyright (c) 2014-2024, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */

import {ClassicEditor} from '@ckeditor/ckeditor5-editor-classic';

import {Alignment} from '@ckeditor/ckeditor5-alignment';
import {Autoformat} from '@ckeditor/ckeditor5-autoformat';
import {Bold, Code, Italic, Underline} from '@ckeditor/ckeditor5-basic-styles';
import {BlockQuote} from '@ckeditor/ckeditor5-block-quote';
import {CloudServices} from '@ckeditor/ckeditor5-cloud-services';
import {CodeBlock} from '@ckeditor/ckeditor5-code-block';
import type {EditorConfig} from '@ckeditor/ckeditor5-core';
import {Essentials} from '@ckeditor/ckeditor5-essentials';
import {FontBackgroundColor, FontColor, FontFamily, FontSize} from '@ckeditor/ckeditor5-font';
import {GeneralHtmlSupport} from '@ckeditor/ckeditor5-html-support';
import {Image, ImageInsert, ImageResize, ImageStyle, ImageToolbar, ImageUpload} from '@ckeditor/ckeditor5-image';
import {Link, LinkImage} from '@ckeditor/ckeditor5-link';
import {List, TodoList} from '@ckeditor/ckeditor5-list';
import {Paragraph} from '@ckeditor/ckeditor5-paragraph';
import {Style} from '@ckeditor/ckeditor5-style';
import {Undo} from '@ckeditor/ckeditor5-undo';

// You can read more about extending the build with additional plugins in the "Installing plugins" guide.
// See https://ckeditor.com/docs/ckeditor5/latest/installation/plugins/installing-plugins.html for details.

class Editor extends ClassicEditor {
  public static override builtinPlugins = [
    Alignment,
    Autoformat,
    BlockQuote,
    Bold,
    CloudServices,
    Code,
    CodeBlock,
    Essentials,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    GeneralHtmlSupport,
    Image,
    ImageInsert,
    ImageResize,
    ImageStyle,
    ImageToolbar,
    ImageUpload,
    Italic,
    Link,
    LinkImage,
    List,
    Paragraph,
    TodoList,
    Underline,
    Style,
    Undo
  ];

  public static override defaultConfig: EditorConfig = {
    toolbar: {
      items: [
        'undo',
        'redo',
        '|',
        'paragraph',
        'style',
        'italic',
        'bold',
        'underline',
        'fontFamily',
        'fontColor',
        'fontSize',
        'fontBackgroundColor',
        'alignment',
        '|',
        'todoList',
        'bulletedList',
        'numberedList',
        '|',
        'blockQuote',
        'code',
        'codeBlock',
        '|',
        'link',
        'imageInsert'
      ]
    },
    language: 'en',
    image: {
      toolbar: [
        'imageTextAlternative',
        'imageStyle:inline',
        'imageStyle:block',
        'imageStyle:side',
        'linkImage'
      ]
    },
    style: {
      definitions: [
        {
          name: 'Code (dark)',
          element: 'pre',
          classes: [ 'fancy-code', 'fancy-code-dark' ]
        },
        {
          name: 'Code (bright)',
          element: 'pre',
          classes: [ 'fancy-code', 'fancy-code-bright' ]
        }
      ]
    }
  };
}

export default Editor;
