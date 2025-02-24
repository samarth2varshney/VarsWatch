package com.example.varswatch.data.repository

import android.util.Log
import com.example.varswatch.data.remote.dto.SearchResultsDto
import com.example.varswatch.data.remote.YoutubeApi
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.model.VideoData
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeApi: YoutubeApi,
    private val firestoreInstance: FirebaseFirestore
) : YoutubeRepository {

    private val channels = "channels"
    private val history = "history"
    private val playList = "playList"

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

    override suspend fun addVideoToHistory(item: Item) {
        firestoreInstance.collection(history).document(item.id.videoId).set(item)
    }


    override suspend fun subscribeToChannel(item: Item): Resource<Boolean> {
        firestoreInstance.collection(channels).document(item.id.channelId).set(item).await()
        return Resource.Success(true)
    }

    override suspend fun unsubscribeToChannel(item: Item): Resource<Boolean> {
        firestoreInstance.collection(channels).document(item.id.channelId).delete().await()
        return Resource.Success(true)
    }

    override suspend fun getSubscribedChannels(): Resource<List<Item>> {
        return try {
            val snapshot = firestoreInstance.collection(channels)
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }

    override suspend fun getHistory(): Resource<List<Item>> {
        return try {
            val snapshot = firestoreInstance.collection(history)
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
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

    override suspend fun getPlayList(playListName: String): Resource<List<Item>> {
        return try {
            val snapshot = firestoreInstance.collection(playList).document(playListName).collection("List")
                .get().await()
            val results = snapshot.documents.mapNotNull { it.toObject(SearchResultsDto.Item::class.java) }.map { it.toSearchResultsItems() }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }

    override suspend fun addVideoToPlayList(item: Item, playListName: String) {
        firestoreInstance.collection(playList).document(playListName).collection("List").document(item.id.videoId).set(item)
    }

    override suspend fun addNewPlayList(playListName: String) {
        firestoreInstance.collection(playList).document(playListName).collection("List")
    }

    override suspend fun getListOfPlayLists(): Resource<List<String>> {
        return try {
            val snapshot = firestoreInstance.collection(playList).get().await()
            val results:MutableList<String> = mutableListOf()
            snapshot.documents.forEach {it->
                results.add(it.id)
            }
            Resource.Success(results)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch history: ${e.message}")
        }
    }


}
