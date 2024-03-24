package com.wildpeppers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wildpeppers.R
import kotlinx.android.synthetic.main.fragment_set_wallpaper.*

class SetWallpaperFragment : Fragment(), View.OnClickListener {

    private var image: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_wallpaper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = SetWallpaperFragmentArgs.fromBundle(requireArguments()).wallpaperImage

        //Set Wallpaper Button Clicked
        wallpaper_set_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.wallpaper_set_btn -> setWallpaper()
        }
    }

    private fun setWallpaper() {
        //Change Text and Disable Button
        wallpaper_set_btn.isEnabled = false
        wallpaper_set_btn.text = "Set wallpaper"
        wallpaper_set_btn.setTextColor(resources.getColor(R.color.black, null))

        val bitmap: Bitmap = view_image.drawable.toBitmap()
        val task = SetWallpaperTask(requireContext(), bitmap)
        task.execute(true)
    }


    companion object {
        class SetWallpaperTask internal constructor(private val context: Context, private val bitmap: Bitmap) :
                AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg params: Boolean?): String? {
                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                return "Wallpaper Set"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (image != null) {
            //Set Image
            Glide.with(requireContext()).load(image).listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            //Image Loaded, Show Set Wallpaper Button
                            wallpaper_set_btn.visibility = View.VISIBLE

                            //Hide Progress
                            wallpaper_loading_progress.visibility = View.INVISIBLE

                            return false
                        }
                    }
            ).into(view_image)
        }
    }

    override fun onStop() {
        super.onStop()
        Glide.with(requireContext()).clear(view_image)
    }
}