<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_Ar_id
 * @property int        $C_Nav_id
 * @property int        $C_Ar_T_id
 * @property string     $C_Ar_name
 * @property string     $C_Ar_file
 * @property Date       $C_Ar_date
 * @property int        $C_St_id

 */
class CArticles extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Articles';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_Ar_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_Nav_id', 'C_Ar_T_id', 'C_Ar_name', 'C_Ar_file', 'C_Ar_date', 'C_St_id'
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
        'C_Ar_id' => 'int', 'C_Nav_id' => 'int', 'C_Ar_T_id' => 'int', 'C_Ar_name' => 'string', 'C_Ar_file' => 'string', 'C_Ar_date' => 'date', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'C_Ar_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_ar_type()
    {
        return $this->belongsTo(CArticleType::class, 'C_Ar_T_id');
    }

    public function eq_nav()
    {
        return $this->belongsTo(CNavigation::class, 'C_Nav_id');
    }

    public function eq_art18n()
    {
        return $this->hasMany(CArticlesLng::class, 'C_Ar_id');
    }
}
