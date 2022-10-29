package com.bsc_bandarlampungsportcenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

  private List<SliderItem> listSliders;
  ViewPager2 viewPager2;

  public SliderAdapter(List<SliderItem> listSliders, ViewPager2 viewPager2) {
    this.listSliders = listSliders;
    this.viewPager2 = viewPager2;
  }

  @NonNull
  @Override
  public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new SliderViewHolder(
      LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
      holder.setImage(listSliders.get(position));
  }

  @Override
  public int getItemCount() {
    return listSliders.size();
  }

  class SliderViewHolder extends RecyclerView.ViewHolder {
    private ImageView photo;

    SliderViewHolder(@NonNull View itemView) {
      super(itemView);
      photo = itemView.findViewById(R.id.imageSlide);
    }
    void setImage( SliderItem sliderItem )
    {
      photo.setImageResource(sliderItem.getImage());
    }
  }

}

