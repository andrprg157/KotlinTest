package com.nearby.wardrobetest.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.nearby.wardrobetest.db.AllDao;
import com.nearby.wardrobetest.db.ApparelDatabase;
import com.nearby.wardrobetest.db.Bottomwear;
import com.nearby.wardrobetest.db.Topwear;
import com.nearby.wardrobetest.db.Wishlist;

import java.util.List;

public class ApparelRepo {

    private AllDao allDao;
    public static Wishlist wishlistData;
    private LiveData<List<Topwear>> getTopWear;
    private LiveData<List<Bottomwear>> getBottomWear;

    public ApparelRepo(Application application){
        ApparelDatabase database = ApparelDatabase.getInstance(application);
        allDao = database.allDao();
        getTopWear = allDao.getTopWear();
        getBottomWear = allDao.getBottomWear();
    }

    public void insertTopwear(Topwear topwear){
        new InsertTopAsyncTask(allDao).execute(topwear);
    }

    public void insertBottomwear(Bottomwear bottomwear){
        new InsertBottomAsyncTask(allDao).execute(bottomwear);
    }

    public void insertWishlist(Wishlist wishlist){
        new InsertWishAsyncTask(allDao).execute(wishlist);
    }

    public void deleteWishlist(Wishlist wishlist) {
        new DeleteWishAsyncTask(allDao).execute(wishlist);
    }

    public Wishlist getWishlistData(int tid,int bid){
        Integer[] intData = new Integer[]{tid,bid};
        new GetWishAsyncTask(allDao).execute(intData);
        return wishlistData;
    }

    public LiveData<List<Topwear>> getTopWear() {
        return getTopWear;
    }

    public LiveData<List<Bottomwear>> gettBottomWear() {
        return getBottomWear;
    }

    private static class InsertTopAsyncTask extends AsyncTask<Topwear,Void,Void>{

        private AllDao allDao;
        public InsertTopAsyncTask(AllDao allDao) {
            this.allDao = allDao;
        }

        @Override
        protected Void doInBackground(Topwear... topwears) {
            allDao.insertTopWear(topwears[0]);
            return null;
        }
    }

    private static class InsertBottomAsyncTask extends AsyncTask<Bottomwear,Void,Void>{

        private AllDao allDao;
        public InsertBottomAsyncTask(AllDao allDao) {
            this.allDao = allDao;
        }

        @Override
        protected Void doInBackground(Bottomwear... bottomwears) {
            allDao.insertBottomWear(bottomwears[0]);
            return null;
        }
    }

    private static class InsertWishAsyncTask extends AsyncTask<Wishlist,Void,Void>{

        private AllDao allDao;
        public InsertWishAsyncTask(AllDao allDao) {
            this.allDao = allDao;
        }

        @Override
        protected Void doInBackground(Wishlist... wishlists) {
            allDao.insertWishlist(wishlists[0]);
            return null;
        }
    }

    private static class DeleteWishAsyncTask extends AsyncTask<Wishlist,Void,Void>{

        private AllDao allDao;
        public DeleteWishAsyncTask(AllDao allDao) {
            this.allDao = allDao;
        }

        @Override
        protected Void doInBackground(Wishlist... wishlists) {
            allDao.deleteWishlist(wishlists[0]);
            return null;
        }
    }

    private static class GetWishAsyncTask extends AsyncTask<Integer,Void,Wishlist>{

        private AllDao allDao;
        public GetWishAsyncTask(AllDao allDao) {
            this.allDao = allDao;
        }

        @Override
        protected Wishlist doInBackground(Integer... integers) {
            return allDao.getWishData(integers[0],integers[1]);
        }

        @Override
        protected void onPostExecute(Wishlist wishlist) {
            super.onPostExecute(wishlist);
            wishlistData = wishlist;
        }
    }


}
