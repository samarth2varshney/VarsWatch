package com.example.varswatch.data.repository

import android.util.Log
import com.example.varswatch.data.remote.dto.SearchResultsDto
import com.example.varswatch.data.remote.YoutubeApi
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.model.VideoData
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import com.example.varswatch.util.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeApi: YoutubeApi,
    private val firestoreInstance: FirebaseFirestore,
    private val sharedPrefManager: SharedPrefManager
) : YoutubeRepository {

    private val channels = "channels"
    private val history = "history"
    private val playList = "playList"
    private var user:DocumentReference
    private val auth = FirebaseAuth.getInstance()

    init {
        if(auth.currentUser!=null && auth.currentUser!!.email!=null) {
            user = firestoreInstance.collection("Users").document(auth.currentUser!!.email!!)
            Log.i("YouTubeRepository",auth.currentUser!!.email!!)
        }else{
            user = firestoreInstance.collection("Users").document("General")
        }
        auth.addAuthStateListener {
            if(it.currentUser!=null && it.currentUser!!.email!=null) {
                user = firestoreInstance.collection("Users").document(it.currentUser!!.email!!)
                Log.i("YouTubeRepository",it.currentUser!!.email!!)
            }
        }
    }

    override suspend fun createNewUser(email: String): Resource<Boolean> {
        return try {
            user = firestoreInstance.collection("Users").document(auth.currentUser!!.email!!)
            return Resource.Success(true)
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }


    override suspend fun getYoutubeData(id: String): Resource<VideoData> {
        return try {
            Resource.Success(
                data = youtubeApi.getVideoData(
                    id
                ).toVideoData()
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

    override suspend fun getSearchResults(query: String): Resource<SearchResults> {
        return try {
            Resource.Success(
                data = youtubeApi.search(query).toSearchResults()
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

    override suspend fun getChannelSearchResults(query: String): Resource<SearchResults> {
        return try {
            Resource.Success(
                data = youtubeApi.searchChannels(query).toSearchResults()
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

    override suspend fun getChannelVideos(channelId: String): Resource<SearchResults> {
        return try {
            Resource.Success(
                data = youtubeApi.getChannelVideos(channelId).toSearchResults()
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

    override suspend fun addVideoToHistory(item: Item) {
        user.collection(history).document(item.id.videoId).set(item)
    }


    override suspend fun subscribeToChannel(item: Item): Resource<Boolean> {
        user.collection(channels).document(item.id.channelId).set(item).await()
        return Resource.Success(true)
    }

    override suspend fun unsubscribeToChannel(item: Item): Resource<Boolean> {
        user.collection(channels).document(item.id.channelId).delete().await()
        return Resource.Success(true)
    }

    override suspend fun getSubscribedChannels(): Resource<List<Item>> {
        return try {
            val snapshot = user.collection(channels)
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }

    override suspend fun getHistory(): Resource<List<Item>> {
        return try {
            val snapshot = user.collection(history)
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }

    override suspend fun getPlayList(playListName: String): Resource<List<Item>> {
        return try {
            val snapshot = user.collection(playList).document(playListName).collection("List")
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }

    override suspend fun addVideoToPlayList(item: Item, playListName: String) {
        user.collection(playList).document(playListName).collection("List").document(item.id.videoId).set(item)
    }

    override suspend fun addNewPlayList(playListName: String) {
        user.collection(playList).document(playListName).collection("List")
    }

    override suspend fun getListOfPlayLists(): Resource<List<String>> {
        return try {
            val snapshot = user.collection(playList).get().await()
            val results:MutableList<String> = mutableListOf()
            snapshot.documents.forEach {
                results.add(it.id)
            }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }


}
