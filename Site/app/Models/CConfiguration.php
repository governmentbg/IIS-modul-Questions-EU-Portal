<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class CConfiguration extends Model
{
    use SoftDeletes;

    protected $table = 'C_Configuration';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'CC_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'CCT_id', 'CC_key', 'CC_value', 'CC_descr'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'CCT_id' => 'int'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    // protected $dates = [
    //      'created_at', 'updated_at'
    // ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    // public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_conf_type()
    {
        // return $this->belongsTo('App\Models\CConfigurationType', 'CCT_id');
        return $this->belongsTo('App\Models\CConfigurationType', 'CCT_id')->select('CCT_id', 'CCT_type');
        // return $this->belongsTo(CConfigurationType::class, 'CCT_id');
    }
    //     public function eq_lang(){
    //     return $this-> belongsTo('App\c_lang','C_Lng_id');
    //   }
}
