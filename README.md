# Recyclerviewsample

Recyclerviewsample is a project aimed to show how to implement a **Recyclerview** and being able to **group items** without seen the performance affected.

This project was going to initially use [ItemDecorators](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ItemDecoration) for the adapter but the offset and scrolling back up doesn't seem to work as nice as it is supposed. So we are using the old trick of different viewholders and render one or the other depending on the type of data.

# Running the project

The project should be runnable by just importing it into yout IntelliJ and running the gradle build command. It has been tested with an Nexus5 Emulator with API 27 and seems to work ok.