package com.example.brand.newsapp;

public class FakeNews {
        private String mSectionName;

        private String mFakeNewsTitle;

        private String mFakeAuthorLastName;

        private String mFakeNewsDate;

        private String mFakeNewsUrl;


        public FakeNews(String sectionName, String fakeNewsTitle, String fakeAuthorLastName, String fakeNewsDate, String fakeNewsUrl) {
            mSectionName = sectionName;
            mFakeNewsTitle = fakeNewsTitle;
            mFakeAuthorLastName = fakeAuthorLastName;
            mFakeNewsDate = fakeNewsDate;
            mFakeNewsUrl = fakeNewsUrl;
        }

        public String getSectionName() {return mSectionName;}
        public String getfakeNewsTitle(){return mFakeNewsTitle;}
         public String getfakeAuthorLastName (){return mFakeAuthorLastName;}
         public String getfakeNewsDate(){return mFakeNewsDate;}
         public String getfakeNewsUrl (){return mFakeNewsUrl;}


}
