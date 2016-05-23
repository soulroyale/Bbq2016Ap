package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jalee.bbqbuddy.helper.ItemTouchHelperAdapter;
import com.jalee.bbqbuddy.helper.ItemTouchHelperViewHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SmartTimer_CardAdapter extends RecyclerView.Adapter<SmartTimer_CardAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    public static Integer cardIndex;
    List<SmartTimer_cardUI> list = new ArrayList<>();
    Boolean PrevActive = false;

    public SmartTimer_CardAdapter(List<SmartTimer_cardUI> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragmant_smart_timer_card_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onItemDismiss(int position) {
        SmartTimer_Service.TimelineList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (!SmartTimer_Service.timerActive && !SmartTimer_Service.timerPaused) {
            String tTitle = SmartTimer_Service.TimelineList.get(fromPosition).getName();
            String tDesc = SmartTimer_Service.TimelineList.get(fromPosition).getsubTitle();
            Integer tMins = SmartTimer_Service.TimelineList.get(fromPosition).getId();
            Integer tImageID = SmartTimer_Service.TimelineList.get(fromPosition).getimageId();

            SmartTimer_Service.TimelineList.remove(fromPosition);

            SmartTimer_Service.TimelineList.add(toPosition, new SmartTimer_cardUI(tDesc, tTitle, tMins,tImageID));

            SmartTimer_Service ST = new SmartTimer_Service();
            ST.saveTimeline(SmartTimer_Service.pubContext);

            //SmartTimer_TimeLine.adapter.notifyDataSetChanged();
            SmartTimer_TimeLine.adapter.notifyItemMoved(fromPosition, toPosition);
        } else {
            if (fromPosition > SmartTimer_Service.nextEventindex && toPosition > SmartTimer_Service.nextEventindex) {

                String tTitle = SmartTimer_Service.TimelineList.get(fromPosition).getName();
                String tDesc = SmartTimer_Service.TimelineList.get(fromPosition).getsubTitle();
                Integer tMins = SmartTimer_Service.TimelineList.get(fromPosition).getId();
                Integer tImageID = SmartTimer_Service.TimelineList.get(fromPosition).getimageId();

                SmartTimer_Service.TimelineList.remove(fromPosition);

                SmartTimer_Service.TimelineList.add(toPosition, new SmartTimer_cardUI(tDesc, tTitle, tMins, tImageID));

                SmartTimer_Service ST = new SmartTimer_Service();
                ST.saveTimeline(SmartTimer_Service.pubContext);

                //SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                SmartTimer_TimeLine.adapter.notifyItemMoved(fromPosition, toPosition);

            }
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.SmartTimer_cardUI = getItem(position);
        holder.cardtitle.setText(list.get(position).name);
        holder.cardsubtitle.setText(list.get(position).subTitle);
        if (list.get(position).imageID == 0) {
            holder.cardimage.setImageResource(R.drawable.cookingicon512px);
        } else{

        }
        if (list.get(position).id == 0) {
            holder.cardmins.setText("-");
        } else {
            holder.cardmins.setText(String.valueOf(list.get(position).id));
        }

        if (SmartTimer_Service.timerActive || SmartTimer_Service.timerPaused) {
            if (position < SmartTimer_Service.nextEventindex) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
                }
                holder.cardtitle.setTextColor(Color.parseColor("#9E9E9E"));
                holder.cardsubtitle.setTextColor(Color.parseColor("#9E9E9E"));
                holder.cardmins.setTextColor(Color.parseColor("#9E9E9E"));
            }

            if (PrevActive & list.get(position).id == 0) {
                holder.cardtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardsubtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardmins.setTextColor(Color.parseColor("#D32F2F"));
            }

            if (position == SmartTimer_Service.nextEventindex) {
                holder.cardtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardsubtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardmins.setTextColor(Color.parseColor("#D32F2F"));
                PrevActive = true;
            } else {
                if (PrevActive & list.get(position).id == 0) {
                    holder.cardtitle.setTextColor(Color.parseColor("#D32F2F"));
                    holder.cardsubtitle.setTextColor(Color.parseColor("#D32F2F"));
                    holder.cardmins.setTextColor(Color.parseColor("#D32F2F"));
                    PrevActive = true;
                } else {
                    PrevActive = false;
                }
            }

        } else {
            PrevActive = false;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            holder.cardtitle.setTextColor(Color.parseColor("#757575"));
            holder.cardsubtitle.setTextColor(Color.parseColor("#757575"));
            holder.cardmins.setTextColor(Color.parseColor("#757575"));

        }

        Animation fadeInAnimation = AnimationUtils.loadAnimation(SmartTimer_Service.pubContext, R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(SmartTimer_Service.pubContext, R.anim.fade_out);
        if (SmartTimer_Service.editing) {
            /*
            holder.cardimage.setImageResource(R.drawable.ic_menu_delete_white);
            holder.cardimage.setBackgroundColor(Color.parseColor("#D32F2F"));
            */
            holder.deleteImage.setBackgroundColor(Color.GRAY);
            holder.cardimage.setVisibility(View.VISIBLE);
            holder.deleteImage.setVisibility(View.VISIBLE);

            fadeInAnimation.setAnimationListener(new Animation.AnimationListener(){
                  @Override public void onAnimationStart(    Animation animation){
                  }
                  @Override public void onAnimationRepeat(    Animation animation){
                  }
                  @Override public void onAnimationEnd(    Animation animation){
                      holder.cardimage.setVisibility(View.INVISIBLE);
                  }
              }
            );
            holder.deleteImage.startAnimation(fadeInAnimation);
            holder.cardimage.startAnimation(fadeOutAnimation);

        } else {
            /*
            holder.cardimage.setImageResource(R.drawable.cookingicon512px);
            holder.cardimage.setBackgroundColor(Color.TRANSPARENT);
            */

            if (holder.deleteImage.getVisibility() == View.VISIBLE) {


                holder.cardimage.setVisibility(View.VISIBLE);
                holder.deleteImage.setVisibility(View.VISIBLE);

                holder.deleteImage.startAnimation(fadeOutAnimation);
                fadeOutAnimation.setAnimationListener(new Animation.AnimationListener(){
                          @Override public void onAnimationStart(    Animation animation){
                                                  }
                          @Override public void onAnimationRepeat(    Animation animation){
                                                  }
                          @Override public void onAnimationEnd(    Animation animation){
                              holder.deleteImage.setVisibility(View.INVISIBLE);
                              }
                          }
                );
                holder.deleteImage.startAnimation(fadeOutAnimation);
                holder.cardimage.startAnimation(fadeInAnimation);
        }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


    public SmartTimer_cardUI getItem(int i) {
        return list.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cardimage;
        ImageView deleteImage;
        TextView cardtitle;
        TextView cardsubtitle;
        TextView cardmins;
        SmartTimer_cardUI SmartTimer_cardUI;
        final Context context = itemView.getContext();

        public ViewHolder(View itemView) {
            super(itemView);
            cardimage = (ImageView) itemView.findViewById(R.id.reorderImage);
            deleteImage = (ImageView) itemView.findViewById(R.id.imageDelete);
            cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
            cardsubtitle = (TextView) itemView.findViewById(R.id.carddesc);
            cardmins = (TextView) itemView.findViewById(R.id.cardmins);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (SmartTimer_Service.editing) {
                        remTimeline(context, getAdapterPosition());
                    }
                }
            });

            /*
            cardtitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remTimeline(context, getAdapterPosition());
                    return true;
                }
            });
            cardsubtitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remTimeline(context, getAdapterPosition());
                    return true;
                }
            });
            */

            cardmins.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (SmartTimer_Service.timelineCanIncrease & !SmartTimer_Service.timerExtend) {
                        decreaseMin(1, getAdapterPosition(), context);
                    }
                    return true;
                }
            });

            cardmins.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (SmartTimer_Service.timelineCanIncrease & !SmartTimer_Service.timerExtend) {
                        increaseMin(1, getAdapterPosition(), context);
                    }
                }
            });

        }

                @Override
        public void onClick(View v) {

        }
    }

    public void increaseMin(int increaseVar, int adapterPos, Context context) {
        SmartTimer_Service.TimelineList.set(adapterPos, new SmartTimer_cardUI(SmartTimer_Service.TimelineList.get(adapterPos).getsubTitle(), SmartTimer_Service.TimelineList.get(adapterPos).getName(), SmartTimer_Service.TimelineList.get(adapterPos).getId() + increaseVar,SmartTimer_Service.TimelineList.get(adapterPos).getimageId()));
        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
        SmartTimer_Service ST = new SmartTimer_Service();
        ST.saveTimeline(context);
        Log.i("info", String.valueOf(adapterPos));
        Log.i("info",String.valueOf(SmartTimer_Service.nextEventindex));
        if (SmartTimer_Service.timerActive & adapterPos == SmartTimer_Service.nextEventindex) {
            SmartTimer_Service.timerChangeBy = 60000L;
            SmartTimer_Service.timerExtend = true;
            Log.i("info", "Timerextend");
        }
        Toast.makeText(context, SmartTimer_Service.TimelineList.get(adapterPos).getName() + " increased by " + increaseVar + " minute", Toast.LENGTH_SHORT).show();
    }

    public void decreaseMin(int decreaseVar, int adapterPos, Context context) {
        SmartTimer_Service.TimelineList.set(adapterPos, new SmartTimer_cardUI(SmartTimer_Service.TimelineList.get(adapterPos).getsubTitle(), SmartTimer_Service.TimelineList.get(adapterPos).getName(), SmartTimer_Service.TimelineList.get(adapterPos).getId() - decreaseVar, SmartTimer_Service.TimelineList.get(adapterPos).getimageId()));
        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
        SmartTimer_Service ST = new SmartTimer_Service();
        ST.saveTimeline(context);
        if (SmartTimer_Service.timerActive & adapterPos == SmartTimer_Service.nextEventindex) {
            SmartTimer_Service.timerChangeBy = 60000L;
            SmartTimer_Service.timerReduce = true;
        }
        Toast.makeText(context, SmartTimer_Service.TimelineList.get(adapterPos).getName() + " decreased by " + decreaseVar + " minute", Toast.LENGTH_SHORT).show();
    }

    public void remTimeline (final Context context, final int adapterPos) {
        Log.i("Button Click", "Button Pressed at: " + adapterPos);
        if (SmartTimer_Service.timerActive == true & adapterPos <= SmartTimer_Service.nextEventindex) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Cannot delete the current event, or en event that has already occured!");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {

            cardIndex = (Integer) adapterPos;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Would you like to Delete the selected item from the timeline?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Log.i("Selected", "You salected yes");
                    SmartTimer_Service.TimelineList.remove(adapterPos);
                    SmartTimer_TimeLine.adapter.notifyItemRemoved(adapterPos);
                    Integer newSmartTimerValue = 0;
                    for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                        newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                        System.out.println(newSmartTimerValue);
                    }
                    SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                    SmartTimer_Service ST = new SmartTimer_Service();
                    ST.saveTimeline(context);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Selected", "You salected no");
                }

            });
            alertDialogBuilder.setNeutralButton("Delete All", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder alertDialogBuilderdelAll = new AlertDialog.Builder(context);
                            alertDialogBuilderdelAll.setMessage("WARNING!! This will clear the entire timeline, are you sure?");
                            alertDialogBuilderdelAll.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Log.i("Selected", "You salected yes");
                                            if (!SmartTimer_Service.timerActive) {
                                                final Handler handler = new Handler();
                                                Runnable run = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        SmartTimer_Service.TimelineList.remove(0);
                                                        SmartTimer_TimeLine.adapter.notifyItemRemoved(0);
                                                        if (SmartTimer_Service.TimelineList.size() > 0) {
                                                            handler.postDelayed(this,400);
                                                        } else {
                                                            SmartTimer_Service.TimelineList.clear();
                                                            Integer newSmartTimerValue = 0;
                                                            for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                                                                newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                                                                System.out.println(newSmartTimerValue);
                                                            }
                                                            SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                                                            SmartTimer_Service ST = new SmartTimer_Service();
                                                            ST.saveTimeline(context);
                                                        }
                                                    }
                                                };
                                                handler.post(run);


                                            } else {
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                                alertDialogBuilder.setMessage("Cannot delete all while timer is active!");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {

                                                    }
                                                });
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();
                                            }

                                        }
                                    }
                            );

                            alertDialogBuilderdelAll.setNegativeButton("No", new DialogInterface.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.i("Selected", "You salected no");
                                        }

                                    }

                            );
                            AlertDialog alertDialog = alertDialogBuilderdelAll.create();
                            alertDialog.show();
                        }
                    }

            );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }


}