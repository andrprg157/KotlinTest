package com.nearby.wardrobetest.vm;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nearby.wardrobetest.db.Bottomwear;
import com.nearby.wardrobetest.db.Topwear;
import com.nearby.wardrobetest.db.Wishlist;
import com.nearby.wardrobetest.repo.ApparelRepo;

import java.util.List;

public class ApparelViewModel extends AndroidViewModel {

    private ApparelRepo apparelRepo;
    private LiveData<List<Topwear>> allTopWear;
    private LiveData<List<Bottomwear>> allBottomWear;
    public ApparelViewModel(Application application) {
        super(application);
        apparelRepo = new ApparelRepo(application);
        allTopWear = apparelRepo.getTopWear();
        allBottomWear = apparelRepo.gettBottomWear();
    }

    public void insertTopWear(Topwear topwear){
        apparelRepo.insertTopwear(topwear);
    }

    public void insertBottomWear(Bottomwear bottomwear){
        apparelRepo.insertBottomwear(bottomwear);
    }

    public void insertWishlist(Wishlist wishlist){
        apparelRepo.insertWishlist(wishlist);
    }

    public void deleteWishlist(Wishlist wishlist){
        apparelRepo.deleteWishlist(wishlist);
    }

    public Wishlist getWishlistData(int tid,int bid){
        return apparelRepo.getWishlistData(tid,bid);
    }

    public LiveData<List<Topwear>> getAllTopWear() {
        return allTopWear;
    }

    public LiveData<List<Bottomwear>> getAllBottomWear() {
        return allBottomWear;
    }
}
