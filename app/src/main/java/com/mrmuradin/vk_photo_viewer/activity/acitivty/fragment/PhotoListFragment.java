package com.mrmuradin.vk_photo_viewer.activity.acitivty.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mrmuradin.vk_photo_viewer.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;


public class PhotoListFragment extends Fragment {

    RecyclerView rvPhotos;
    PhotosAdapter photosAdapter;
    VKApiUserFull user;
    VKList<VKApiPhoto> photoList = new VKList<>();
    DialogFragment photoViewDialog;


    public static PhotoListFragment newInstance() {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photo_list, container, false);

        photosAdapter = new PhotosAdapter();

        rvPhotos = (RecyclerView) view.findViewById(R.id.rv_photos);
        rvPhotos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPhotos.setAdapter(photosAdapter);

        DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag(PhotoViewDialogFragment.class.getSimpleName());
        if (dialog != null){
//            dialog.show(getFragmentManager(), PhotoViewDialogFragment.class.getSimpleName());
        }

        getMe();

        return view;
    }


    private void getMe() {
        VKRequest userRequest = VKApi.users().get();
        userRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList list = (VKList) response.parsedModel;
                user = (VKApiUserFull) list.get(0);
                getAlbums(user.getId());
            }
        });
    }

    private void getAlbums(int id) {
        VKRequest requestCreatedAlbums = new VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, id, "need_covers", 1));
        requestCreatedAlbums.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiPhotoAlbum> createdAlbums = new VKList<>(response.json, VKApiPhotoAlbum.class);
                for (int i = 0; i < createdAlbums.size(); i++) {
                    VKApiPhotoAlbum album = createdAlbums.get(i);
                    loadPhotosForAlbum(album.getId());
                }
            }
        });
    }

    private void loadPhotosForAlbum(int albumId) {
        VKRequest photosRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID, user.getId(), VKApiConst.ALBUM_ID, albumId));
        photosRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiPhoto> photos = new VKList<>(response.json, VKApiPhoto.class);
                photoList.addAll(photos);
                photosAdapter.notifyDataSetChanged();

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_photo_item);
        }


    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int itemPosition = rvPhotos.getChildPosition(v);
            VKApiPhoto photoModel = photoList.get(itemPosition);
            String photo = getPhotoSize(photoModel);

            photoViewDialog = PhotoViewDialogFragment.newInstance(photo);
            photoViewDialog.show(getFragmentManager(), PhotoViewDialogFragment.class.getSimpleName());

        }
    }

    class PhotosAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
            v.setOnClickListener(new MyOnClickListener());
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            VKApiPhoto photoModel = photoList.get(position);
            Glide.with(getActivity())
                    .load(getPhotoSize(photoModel))
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }

    protected static String getPhotoSize(VKApiPhoto photo) {
        String result = "";
        if (!photo.photo_1280.isEmpty()) {
            result = photo.photo_1280;
        } else if (!photo.photo_807.isEmpty()) {
            result = photo.photo_807;
        } else if (!photo.photo_604.isEmpty()) {
            result = photo.photo_604;
        } else if (!photo.photo_130.isEmpty()) {
            result = photo.photo_130;
        } else if (!photo.photo_75.isEmpty()) {
            result = photo.photo_75;
        }
        return result;

    }


}
