<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_ArL_id
 * @property int        $C_Ar_id
 * @property int        $C_Lang_id
 * @property string     $C_ArL_title
 * @property string     $C_ArL_body
 * @property int        $C_St_id

 */
class CArticlesLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Articles_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_ArL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_Ar_id', 'C_Lang_id', 'C_ArL_title', 'C_ArL_body', 'C_St_id'
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
        'C_ArL_id' => 'int', 'C_Ar_id' => 'int', 'C_Lang_id' => 'int', 'C_ArL_title' => 'string', 'C_ArL_body' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
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
    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }

    public function eq_ar()
    {
        return $this->belongsTo(CArticles::class, 'C_Ar_id');
    }
}
