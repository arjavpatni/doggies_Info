package com.arjavp.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjavp.dogs.R
import com.arjavp.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //instantiate viewModel
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        //instantiate dog list
        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }
        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.dogs.observe(this, Observer {dogs->
            //dogs is a list of type DogBreed
            dogsList.visibility - View.VISIBLE
            dogsListAdapter.updateDogList(dogs)
        })

        viewModel.dogsLoadError.observe(this, Observer {isError->
            //isError is a boolean
            isError?.let {
                listError.visibility=if(it) View.VISIBLE else View.GONE//making error message visible
            }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            //isLoading a boolean
            loadingView.visibility = if(isLoading) View.VISIBLE else View.GONE
            if(isLoading){
                listError.visibility = View.GONE
                dogsList.visibility = View.GONE
            }
        })
    }

}
