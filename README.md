# Pre-work - *SimpleToDoApp*

**SimpleToDoApp** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Shafeeque Qazi**

Time spent: **120** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [X] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] Android Persistence Pattern involving Room , Dao and LiveData!
* [X] Slide left to delete ; Slide right to edit.
* [X] Loading initial data from JSON source!
* [X] Clean interfaces defined for Task. Will plugin Google Task API in future to save tasks to Cloud back-end.
* [X] Fragment and clean communication between fragments , activity using interfaces.
* [X] Recycler View implementation and Custom Adapter and ViewHolder.
* [X] SwipeRefreshLayout implementation. 
* [X] ActionMode implementation.
* [X] NavigationDrawer implementation.
* [X] Search of Tasks.
* [X] Empty View - handling of zero tasks.
* [X] DividerItemDecoration for line splits in RecyclerView of tasks.


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** [ My primary background has been on Windows platform. Android has made things very easy with layouts , styles and clean segregation of resources. However I found working with the design view very tediuos. Also trying out different layouts and seeing results in the emulator is time-consuming. Struggled many nights with Relative Layout. And things get jumbled and messy soon.

While I understand things have been made very easy ; it would be good to draw and test out the UI layout quick-and-dirty somewhere. And then integrate into code.

Maybe I do not know of a faster way of doing this as yet. Adding all the biolerplate code in Android Studio was very easy. It was clean to implement data hiding. And seperation of Activities, Fragments , Adapters etc. is quite neat.].

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** [Enter your answer here in a paragraph or two].

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
