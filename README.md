# Pre-work - *SimpleToDoApp*

**SimpleToDoApp** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Shafeeque Qazi**

Time spent: **95** hours spent in total

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

<img src='/art/Simpletodo4.gif?raw=true' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:**  My primary background has been on Windows platform. Android has made things very easy with layouts , styles and clean segregation of resources. However I found working with the design view very tediuos. Also trying out different layouts and seeing results in the emulator is time-consuming. Struggled many nights with Relative Layout. And things get jumbled and messy soon.

While I understand things have been made very easy ; it would be good to draw and test out the UI layout quick-and-dirty somewhere. And then integrate into code.

Maybe I do not know of a faster way of doing this as yet. Adding all the biolerplate code in Android Studio was very easy. It was clean to implement data hiding. And seperation of Activities, Fragments , Adapters etc. is quite neat.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** [ArrayAdapter is an implementation of the Adapter pattern. It is of great help to list-views and other complex views that have an underlying data model that requires constant binding to their elements. ArrayAdapter interacts with the internal data-model. It is the sole input for the ListView to fetch data. We can implement a CustomAdapter deriving from this and handle communication with various components in the application using interfaces. See my implementation for more details
  Purpose of ConvertView - I have implemented a custom RecyclerViewAdapter in my implementation. convertView in general represents older view reference passed at the time of binding. When fast scrolling it is important to avoid recreating views and binding. Instead the olderview can be saved in a ViewHolder and only the data binding updated on the view-holder].

## Notes

Describe any challenges encountered while building the app.
**Answer:** 
1) Overload of information. When you are starting new, you do not know where to start from. There is so much of information. And I wanted to build a cool-looking app. Learning so many things in a short span of time was challenging. I also found that the Code-Path guides though conscise, saved a lot of time. So for most of my later implementation, I referred to them ( DialogFragment, ActionMode , Fragment and Activity lifeCycle management )
2) Android layouts though simplest, I struggled many nights in looking my views look decent. Layouts can get messed up very quickly.
3) Android studio runtime errors. I spent so many long hours struggling with runtime errors. As when application crashes I was not getting a valid call-stack. And then it was trial-and-error trying to remove code that might be causing it. A rule of thumb is to practice defensive coding ( null-checks , exception handling , log errors and information) so that you can trace failures easily. Sometimes you want to do things fast, but it can be counter-productive.
4) Started with SQLLite implementation using the raw SQLLite classes. There is no syntax check on the embedded SQL queries which are in the form of Strings. You discover problems only at run-time. Leveraging Room and persistence library that use decorations and compile-time checks was very helpful.
5) Was trying to build a compatible Task interface with Google Task API, so that can upload to Cloud API in future. This proved to be a big time-waster. As binding and converting every field in the Google Task API was time-consuming. Only relevant fields are title and priority. But the time spent on building this taught many things about JSON parsing and clean interface implementation. 
6) Implemented Splash-Screen very early-on and my Android knowledge was weak at that time. There are so many different advices to do this. Hope there was one clean documented way of doing this right.
7) Sliding left and right animation - Had to read a lot to understand this. Eventually did not use any library, but drawing the background color and icons manually in the OnChildDraw of ItemTouchHelperCallback implementation.

## License

    Copyright [2017] [Shafeeque Qazi]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
